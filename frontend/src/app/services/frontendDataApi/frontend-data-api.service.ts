import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';

export interface BlogPost {
  title: string;
  path: string;
}

@Injectable({
  providedIn: 'root'
})
export class FrontendDataApiService {
  private http = inject(HttpClient)

  constructor() { }

  getBlogPosts(): Observable<BlogPost[]> {
    return this.http.get<BlogPost[]>('http://localhost:8080/api/frontenddata/posts');
  }
}
