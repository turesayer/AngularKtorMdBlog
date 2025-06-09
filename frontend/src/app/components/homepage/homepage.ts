import {Component} from '@angular/core';
import {BlogPost} from '../blog-post/blog-post';

@Component({
  selector: 'app-homepage',
  imports: [
  //   MarkdownModule,
    BlogPost
  ],
  templateUrl: './homepage.html',
  styleUrl: './homepage.scss'
})
export class Homepage {
  blogPosts = [
    { title: 'Example Post 01', path: '/content/example-post-01.md' },
    { title: 'Example Post 02', path: '/content/example-post-02.md' }
  ]
}
