import {Component, Input, OnChanges, signal, SimpleChanges} from '@angular/core';
import {MarkdownComponent} from 'ngx-markdown';
import {MatCardModule} from '@angular/material/card';
import {BlogPost, FrontendDataApiService} from '../../services/frontendDataApi/frontend-data-api.service';

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
  @Input() blogPostMetadata: BlogPost | undefined = undefined;

  markdown = signal('');

  constructor(
    private frontendDataApi: FrontendDataApiService
  ) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes["blogPostMetadata"] && this.blogPostMetadata) {
      this.loadMarkdownContent();
    }
  }

  private loadMarkdownContent() {
    if (!this.blogPostMetadata?.filename) {
      console.warn('No markdown filename provided')
      return;
    }

    this.frontendDataApi.getBlogPostContent(this.blogPostMetadata.filename)
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
