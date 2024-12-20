import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PostService } from '../post.service';

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css'],
})
export class PostListComponent implements OnInit {
  posts: any[] = []; // Initialize posts to avoid null issues
  statuses = ['Published', 'Draft', 'Scheduled']; // Example statuses

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.loadPosts();
  }

  loadPosts(): void {
    this.postService.getAllPosts().subscribe((data) => {
      this.posts = data;
    });
  }

  filterByStatus(event: Event): void {
    const selectElement = event.target as HTMLSelectElement; // Type assertion
    const status = selectElement.value;
    if (status) {
      this.postService.getPostsByStatus(status).subscribe((data) => {
        this.posts = data;
      });
    } else {
      this.loadPosts(); // Reload all posts if "All" is selected
    }
  }

  deletePost(id: number | undefined): void {
    if (id && confirm('Are you sure you want to delete this post?')) {
      this.postService.deletePost(id).subscribe(() => {
        alert('Post deleted successfully');
        this.loadPosts();
      });
    }
  }
}