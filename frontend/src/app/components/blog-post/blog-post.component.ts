import {Component, Input, OnChanges, signal, SimpleChanges} from '@angular/core';
import {MarkdownComponent} from 'ngx-markdown';
import {MatCardModule} from '@angular/material/card';
import {FrontendDataApiService} from '../../services/frontendDataApi/frontend-data-api.service';

@Component({
  selector: 'app-blog-post',
  imports: [
    MarkdownComponent,
    MatCardModule
  ],
  templateUrl: './blog-post.component.html',
  styleUrl: './blog-post.component.scss'
})
export class BlogPostComponent implements OnChanges {
  @Input() blogPostPath: string = '';

  markdown = signal('');

  constructor(
    private frontendDataApi: FrontendDataApiService
  ) {
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

    this.frontendDataApi.getBlogPostContent(this.blogPostPath)
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
