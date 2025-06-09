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
    { title: 'Example Post 01', path: '/content/example-post-01.md' },
    { title: 'Example Post 02', path: '/content/example-post-02.md' }
  ]
}
