import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, take } from 'rxjs';

export interface AppConfig {
  baseUrl: string;
}

@Injectable({ providedIn: 'root' })
export class AppConfigService {
  private config: AppConfig = { baseUrl: '' };

  loaded = false;

  constructor(private http: HttpClient) {}

  loadConfig(): Observable<void> {
    return this.http.get<AppConfig>('/assets/app.config.json').pipe(
      map((config) => {
        this.config = config;
        this.loaded = this.loaded;
      }),
      take(1)
    );
  }

  getConfig(): AppConfig {
    return this.config;
  }
}
