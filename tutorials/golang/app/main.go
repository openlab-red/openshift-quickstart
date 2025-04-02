package main

import (
	"context"
	"database/sql"
	"encoding/json"
	"fmt"
	"net/http"
	"os"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	_ "github.com/jackc/pgx/v5/stdlib"
)

type Message struct {
	ID   int    `json:"id"`
	Text string `json:"text"`
}

type DatabaseConfig struct {
	Host     string `json:"host"`
	Port     string `json:"port"`
	Username string `json:"username"`
	Password string `json:"password"`

	Database string `json:"database"`
}

func list(c *gin.Context) {

	config, err := loadConfig("config.json")

	if err != nil {
		// handle error
		return
	}

	db, err := sql.Open("pgx", fmt.Sprintf("postgres://%s:%s@%s:%s/%s",
		config.Username, config.Password, config.Host, config.Port, config.Database))

	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	defer db.Close()

	rows, err := db.QueryContext(context.Background(), "SELECT * FROM messages")
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	defer rows.Close()

	var messages []Message
	for rows.Next() {
		var m Message
		err := rows.Scan(&m.ID, &m.Text)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}
		messages = append(messages, m)
	}

	if err := rows.Err(); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, messages)
}

func add(c *gin.Context) {

	config, err := loadConfig("config.json")
	message := c.Param("message")

	if err != nil {
		// handle error
		return
	}

	db, err := sql.Open("pgx", fmt.Sprintf("postgres://%s:%s@%s:%s/%s",
		config.Username, config.Password, config.Host, config.Port, config.Database))

	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}
	defer db.Close()

	stmt, err := db.Prepare("INSERT INTO messages (text) VALUES ($1)")
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	defer stmt.Close()

	_, err = stmt.Exec(message)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusCreated, gin.H{"message": "Message added successfully"})

}

func ping(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"message": "pong"})
}

func welcome(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"message": "Welcome!"})
}

func main() {
	router := gin.Default()
	router.Use(cors.Default())

	router.GET("/", welcome)
	contextPath := "/api"
	router.GET(contextPath, list)
	router.GET(contextPath+"/add/:message", add) // for browser demo purpose
	router.POST(contextPath+"/add/:message", add)
	router.GET(contextPath+"/ping", ping)
	router.Run()
}

func loadConfig(fileName string) (DatabaseConfig, error) {
	var config DatabaseConfig
	file, err := os.Open(fileName)
	if err != nil {
		return config, err
	}
	defer file.Close()

	decoder := json.NewDecoder(file)
	err = decoder.Decode(&config)
	if err != nil {
		return config, err
	}

	return config, nil

}
