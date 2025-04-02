import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MessageComponent } from '../message/message.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MessageComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'app';
}
