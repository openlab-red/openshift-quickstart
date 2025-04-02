import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService, Message, Pong } from './message.service';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import  
 { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';  

@Component({
  selector: 'app-message',
  standalone: true,
  imports: [FormsModule, 
    MatCardModule,
    MatListModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule
  ],
  templateUrl: './message.component.html',
  styleUrl: './message.component.scss',
})
export class MessageComponent {

    messages: Message[] = [];
    pong: Pong = {message: ''};
    newMessage: string = '';
  
    constructor(private messageService: MessageService) {}
  
    ngOnInit() {
      this.messageService.getMessages().subscribe(messages => {
        this.messages = messages;
        console.log(this.messages);
      });
    }
  
    addMessage() {
      this.messageService.addMessage(this.newMessage).subscribe(
        () => {
          console.log('Message added successfully');
          this.newMessage = '';
          // Refresh message list
          this.messageService.getMessages().subscribe(messages => {
            this.messages = messages;
          });
        },
        error => {
          console.error('Error adding message:', error);
        }
      );
    }
  
    ping() {
      this.messageService.ping().subscribe(pong => {
        this.pong = pong;
        console.log(this.pong.message);
      });
    }
  }
