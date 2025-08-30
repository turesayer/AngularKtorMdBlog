import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {map, Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

export interface BlogPost {
  title: string;
  date: Date;
  filename: string;
}

type RawBlogPost = Omit<BlogPost, 'date'> & { date: string };

@Injectable({
  providedIn: 'root'
})
export class FrontendDataApiService {
  private http = inject(HttpClient)
  private apiBaseUrl = environment.apiBaseUrl;

  constructor() { }

  getBlogPosts(): Observable<BlogPost[]> {
    return this.http
      .get<RawBlogPost[]>(`${this.apiBaseUrl}/frontenddata/posts`)
      .pipe(
        map(posts => posts.map(rawPost => {
          const parsed = new Date(rawPost.date);
          return {...rawPost, date: parsed};
        }))
      )
  }

  getBlogPostContent(fileName: string): Observable<string> {
    return this.http.get(`${this.apiBaseUrl}/frontenddata/posts/${fileName}`, {responseType: "text"});
  }
}
