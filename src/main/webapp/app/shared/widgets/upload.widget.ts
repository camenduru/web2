/* eslint-disable */
import { Component } from '@angular/core';
import { ControlWidget } from 'ngx-schema-form';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'sf-upload-widget',
  templateUrl: 'upload.widget.html',
  imports: [CommonModule],
})
export class UploadWidget extends ControlWidget {
  selectedFile: File | null = null;
  uploadResponse: any = null;
  uploadSuccess: boolean = false;
  errorMessage: string = '';
  fileUrl: string | null = null;

  constructor(private http: HttpClient) {
    super();
  }

  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.uploadFile();
    }
  }

  uploadFile(): void {
    if (!this.selectedFile) {
      return;
    }
    const formData = new FormData();
    formData.append('files[]', this.selectedFile, this.selectedFile.name);
    const uploadUrl = `https://uguu.se/upload?output=json`;
    // const uploadUrl = `https://cors-anywhere.herokuapp.com/https://uguu.se/upload?output=json`;
    this.http
      .post(uploadUrl, formData, {
        headers: new HttpHeaders({
          Accept: `application/json`,
        }),
      })
      .subscribe(
        (response: any) => {
          this.uploadResponse = response;
          this.uploadSuccess = response.success;
          if (this.uploadSuccess && response.files && response.files.length > 0) {
            this.fileUrl = response.files[0].url;
            if (this.fileUrl) {
              this.updateTextarea(this.fileUrl);
            }
          } else {
            this.errorMessage = 'Upload failed, no files returned.';
          }
        },
        error => {
          this.uploadResponse = null;
          this.uploadSuccess = false;
          this.errorMessage = error.message || 'An error occurred during upload.';
        },
      );
  }

  private updateTextarea(url: string): void {
    const inputImageControl = this.formProperty.findRoot().getProperty(this.schema.property);
    if (inputImageControl) {
      inputImageControl.setValue(url, false);
    }
  }
}
