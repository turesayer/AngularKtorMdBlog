import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

export interface BlogPost {
  title: string;
  path: string;
}

@Injectable({
  providedIn: 'root'
})
export class FrontendDataApiService {
  private http = inject(HttpClient)
  private apiBaseUrl = environment.apiBaseUrl;

  constructor() { }

  getBlogPosts(): Observable<BlogPost[]> {
    return this.http.get<BlogPost[]>(`${this.apiBaseUrl}/frontenddata/posts`);
  }
}
