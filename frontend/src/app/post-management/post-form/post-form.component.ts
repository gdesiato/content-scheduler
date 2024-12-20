import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PostService } from '../post.service';

@Component({
  selector: 'app-post-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './post-form.component.html',
  styleUrls: ['./post-form.component.css'],
})
export class PostFormComponent implements OnInit {
  post = { title: '', content: '', status: 'Draft' };
  postId: number | null = null;

  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.postId = this.route.snapshot.params['id'];
    if (this.postId) {
      this.postService.getPostById(this.postId).subscribe((data) => {
        this.post = data;
      });
    }
  }

  savePost(): void {
    if (this.postId) {
      this.postService.updatePost(this.postId, this.post).subscribe(() => {
        alert('Post updated successfully');
        this.router.navigate(['/posts']);
      });
    } else {
      this.postService.createPost(this.post).subscribe(() => {
        alert('Post created successfully');
        this.router.navigate(['/posts']);
      });
    }
  }
}
