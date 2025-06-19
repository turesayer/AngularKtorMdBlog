import {HttpClient} from '@angular/common/http';
import {Component, Input, OnChanges, OnInit, signal, SimpleChanges} from '@angular/core';
import {MarkdownComponent} from 'ngx-markdown';
import {MatCardModule} from '@angular/material/card';

@Component({
  selector: 'app-blog-post',
  imports: [
    MarkdownComponent,
    MatCardModule
  ],
  templateUrl: './blog-post.component.html',
  styleUrl: './blog-post.component.scss'
})
export class BlogPostComponent implements OnInit, OnChanges {
  @Input() blogPostPath: string = '';

  markdown = signal('');

  constructor(
    private http: HttpClient,
  ) {
  }

  ngOnInit() {
    if (this.blogPostPath) {
      this.loadMarkdownContent();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["blogPostPath"] && this.blogPostPath) {
      this.loadMarkdownContent();
    }
  }

  private loadMarkdownContent() {
    if (!this.blogPostPath) {
      console.warn('No markdown path provided')
      return;
    }

    this.http.get(this.blogPostPath, { responseType: 'text' })
      .subscribe({
        next: (content) => {
          this.markdown.set(content);
        },
        error: (error) => {
          console.error('Error loading markdown file:', error);
        }
      });
  }
}
