const express = require('express');
const path = require('path');
const cors = require('cors');
const Pool = require('pg').Pool;
const compression = require("compression");
const fs = require('fs');

const app = express();
const port = process.env.PORT || 8080;
const configPath = path.join(__dirname, 'config.json');
const config = JSON.parse(fs.readFileSync(configPath,  
  'utf8'));

const pool = new Pool(config.database);

app.use(compression());
app.use(cors());
app.use(express.json());

app.use(express.static(path.join(__dirname, "public")));

app.get('/api/', async (req, res) => {
  try {
    const client = await pool.connect();
    const result = await client.query('SELECT * FROM messages');
    const messages = result.rows;
    res.json(messages);
    client.release();
  } catch (err) {
    console.error(err);
    res.status(500).send('Error retrieving messages');
  }
});

app.post('/api/add/:message', async (req, res) => {
  const text = req.params.message;
  try {
    const client = await pool.connect();
    const result = await client.query('INSERT INTO messages (text) VALUES ($1) RETURNING *', [text]);
    const message = result.rows[0];
    res.status(201).json(message);
    client.release();
  } catch (err) {
    console.error(err);
    res.status(500).send('Error adding message');
  }
});

// just for the demo
app.get('/api/add/:message', async (req, res) => {
  const  text = req.params.message;
  try {
    const client = await pool.connect();
    const result = await client.query('INSERT INTO messages (text) VALUES ($1) RETURNING *', [text]);
    const message = result.rows[0];
    res.status(201).json(message);
    client.release();
  } catch (err) {
    console.error(err);
    res.status(500).send('Error adding message');
  }
});

app.get('/api/ping', (req, res) => {
  res.json({ message: 'pong' });
});

app.get('/', (req, res) => {
    res.json({ message: 'Welcome' });
  });

app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});