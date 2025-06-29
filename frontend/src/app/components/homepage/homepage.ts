import {Component} from '@angular/core';
import {BlogPost} from '../blog-post/blog-post';
import {MatCardModule} from '@angular/material/card';

@Component({
  selector: 'app-homepage',
  imports: [
    BlogPost,
    MatCardModule
  ],
  templateUrl: './homepage.html',
  styleUrl: './homepage.scss'
})
export class Homepage {
  blogPosts = [
    { title: 'A Simple Blog Engine', path: '/content/2025-06-15-base-blog-engine.md' },
    { title: 'Hello World', path: '/content/2025-06-14-hello-world.md' }
  ]
}
