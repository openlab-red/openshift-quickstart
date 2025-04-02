import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { AppConfigService } from '../app/app.config.service';

export interface Message {
  id: number;
  text: string;
}

export interface Pong {
  message: string;
}
@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private apiUrl;

  constructor(
    private http: HttpClient,
    private appConfigService: AppConfigService
  ) {
    this.apiUrl =
      this.appConfigService.getConfig().baseUrl || environment.baseUrl;
  }

  getMessages(): Observable<Message[]> {
    return this.http.get<Message[]>(this.apiUrl);
  }

  addMessage(message: string): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/add/${message}`, null);
  }

  ping(): Observable<Pong> {
    return this.http.get<Pong>(this.apiUrl + '/ping');
  }
}
