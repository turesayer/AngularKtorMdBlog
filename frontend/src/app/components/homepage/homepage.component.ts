import {Component, inject, OnInit, signal} from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import {BlogPost, FrontendDataApiService} from '../../services/frontendDataApi/frontend-data-api.service';
import {BlogPostComponent} from '../blog-post/blog-post.component';

@Component({
  selector: 'app-homepage',
  imports: [
    BlogPostComponent,
    MatCardModule
  ],
  templateUrl: './homepage.component.html',
  styleUrl: './homepage.component.scss'
})
export class HomepageComponent implements OnInit {
  private frontendDataApi = inject(FrontendDataApiService);

  blogPosts = signal<BlogPost[]>([]);

  ngOnInit() {
    this.frontendDataApi.getBlogPosts()
      .subscribe(result => {
        this.blogPosts.set(result);
      })
  }
}
