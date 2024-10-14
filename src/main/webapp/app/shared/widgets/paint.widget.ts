/* eslint-disable */
import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, HostListener } from '@angular/core';
import { ControlWidget } from 'ngx-schema-form';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'sf-paint-widget',
  templateUrl: 'paint.widget.html',
  imports: [CommonModule, FormsModule],
})
export class PaintWidget extends ControlWidget implements OnInit, AfterViewInit {
  selectedFile: File | null = null;
  uploadResponse: any = null;
  uploadSuccess: boolean = false;
  fileUrl: string | null = null;

  @ViewChild('canvas') canvasRef!: ElementRef<HTMLCanvasElement>;

  brushSize = 50;
  isDrawing = false;
  isLoading = false;
  isSaving = false;
  history: ImageData[] = [];
  historyIndex = -1;
  errorMessage: string | null = null;
  loadedImageWidth: number | null = null;
  loadedImageHeight: number | null = null;

  private context: CanvasRenderingContext2D | null = null;
  private imageElement: HTMLImageElement | null = null;
  private lastPosition: { x: number; y: number } | null = null;
  private backgroundImage: HTMLImageElement | null = null;

  constructor(private http: HttpClient) {
    super();
  }

  ngOnInit(): void {
    this.setupCanvas(500, 500);
    this.saveToHistory();
  }

  ngAfterViewInit(): void {
    this.context = this.canvasRef.nativeElement.getContext('2d');
    this.updateContext();
  }

  setupCanvas(width: number, height: number): void {
    const canvas = this.canvasRef.nativeElement;
    canvas.width = width;
    canvas.height = height;
    this.updateContext();
  }

  updateContext(): void {
    if (this.context) {
      this.context.lineCap = 'round';
      this.context.lineJoin = 'round';
      this.context.lineWidth = this.brushSize;
    }
  }

  loadImage(): void {
    const inputImageControl = this.formProperty.findRoot().getProperty(this.schema.input_image);
    this.isLoading = true;
    this.errorMessage = null;
    const img = new Image();
    img.crossOrigin = 'anonymous';
    img.src = inputImageControl._value;
    img.onload = () => {
      this.isLoading = false;
      this.loadedImageWidth = img.width;
      this.loadedImageHeight = img.height;

      if (this.context) {
        const canvasWidth = img.width;
        const canvasHeight = img.height;
        this.setupCanvas(canvasWidth, canvasHeight);
        this.drawImageToFitCanvas(img);
      }
      this.backgroundImage = img;
      this.adjustBrushSize();
      this.saveToHistory();
    };
    img.onerror = () => {
      this.isLoading = false;
      this.errorMessage = 'Failed to load the image. Please check the URL and try again.';
    };
  }

  drawImageToFitCanvas(img: HTMLImageElement): void {
    const canvas = this.canvasRef.nativeElement;
    const ctx = this.context;

    if (!ctx) {
      return;
    }

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const drawWidth = canvas.width;
    const drawHeight = (canvas.width * img.height) / img.width;

    const xOffset = (canvas.width - drawWidth) / 2;
    const yOffset = (canvas.height - drawHeight) / 2;

    ctx.drawImage(img, xOffset, yOffset, drawWidth, drawHeight);
    this.updateContext();
  }

  startDrawing(event: MouseEvent): void {
    this.isDrawing = true;
    const { offsetX, offsetY } = this.getMousePos(event);
    this.lastPosition = { x: offsetX, y: offsetY };
  }

  stopDrawing(): void {
    this.isDrawing = false;
    this.lastPosition = null;
    this.context?.beginPath();
    this.saveToHistory();
  }

  draw(event: MouseEvent): void {
    if (!this.isDrawing || !this.context || !this.lastPosition) {
      return;
    }

    const { offsetX, offsetY } = this.getMousePos(event);

    // Use 'destination-out' for erasing pixels
    this.context.globalCompositeOperation = 'destination-out';

    this.context.beginPath();
    this.context.moveTo(this.lastPosition.x, this.lastPosition.y);
    this.context.lineTo(offsetX, offsetY);
    this.context.stroke();

    this.lastPosition = { x: offsetX, y: offsetY };

    // Reset globalCompositeOperation to default
    this.context.globalCompositeOperation = 'source-over';
  }

  clearCanvas(): void {
    if (this.context) {
      this.context.clearRect(0, 0, this.canvasRef.nativeElement.width, this.canvasRef.nativeElement.height);
      this.updateContext();
      this.imageElement = null;
      this.loadedImageWidth = null; // Reset loaded image width
      this.loadedImageHeight = null; // Reset loaded image height
      this.saveToHistory();
    }
  }

  saveImage(): void {
    if (this.loadedImageWidth === null || this.loadedImageHeight === null) {
      console.error('No image loaded to save.');
      return;
    }
    this.isSaving = true;
    const tempCanvas = document.createElement('canvas');
    tempCanvas.width = this.loadedImageWidth;
    tempCanvas.height = this.loadedImageHeight;
    const tempCtx = tempCanvas.getContext('2d');

    if (tempCtx) {
      // Draw the current canvas content (which includes the drawings)
      tempCtx.drawImage(this.canvasRef.nativeElement, 0, 0);

      // Convert the temporary canvas to a blob
      tempCanvas.toBlob(blob => {
        if (blob) {
          // Create a File from the Blob
          this.selectedFile = new File([blob], 'mask.png', { type: 'image/png' });

          // // Create a URL for the Blob and trigger download
          // const url = URL.createObjectURL(blob);
          // const link = document.createElement('a');
          // link.href = url;
          // link.download = 'mask.png';
          // document.body.appendChild(link);
          // link.click();
          // document.body.removeChild(link);
          // URL.revokeObjectURL(url);

          // Upload the file if the schema specifies an upload URL
          if (this.schema.upload_url.includes('uguu')) {
            this.uploadFileToUguu();
          } else if (this.schema.upload_url.includes('catbox')) {
            this.uploadFileToLitterbox();
          }
        }
      }, 'image/png');
    }
  }

  saveToHistory(): void {
    if (this.context) {
      const newHistory = this.history.slice(0, this.historyIndex + 1);
      newHistory.push(this.context.getImageData(0, 0, this.canvasRef.nativeElement.width, this.canvasRef.nativeElement.height));
      this.history = newHistory;
      this.historyIndex = newHistory.length - 1;
    }
  }

  undo(): void {
    if (this.historyIndex > 0) {
      this.historyIndex--;
      this.loadFromHistory(this.historyIndex);
    }
  }

  loadFromHistory(index: number): void {
    if (this.context) {
      this.context.putImageData(this.history[index - 1], 0, 0);
    }
  }

  getMousePos(event: MouseEvent): { offsetX: number; offsetY: number } {
    const rect = this.canvasRef.nativeElement.getBoundingClientRect();
    const scaleX = this.canvasRef.nativeElement.width / rect.width;
    const scaleY = this.canvasRef.nativeElement.height / rect.height;

    return {
      offsetX: (event.clientX - rect.left) * scaleX,
      offsetY: (event.clientY - rect.top) * scaleY,
    };
  }

  adjustBrushSize(): void {
    if (this.loadedImageWidth) {
      const baseSize = 32;
      this.brushSize = (this.loadedImageWidth / baseSize) * 4;
      this.updateContext();
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize(): void {
    if (this.backgroundImage) {
      this.setupCanvas(this.loadedImageWidth!, this.loadedImageHeight!);
      this.drawImageToFitCanvas(this.backgroundImage);
    }
  }

  uploadFileToUguu(): void {
    if (!this.selectedFile) {
      return;
    }
    const formData = new FormData();
    formData.append('files[]', this.selectedFile, this.selectedFile.name);
    const uploadUrl = this.schema.upload_url;
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

  uploadFileToLitterbox(): void {
    if (!this.selectedFile) {
      return;
    }
    const formData = new FormData();
    formData.append('reqtype', 'fileupload');
    formData.append('time', '72h');
    formData.append('fileToUpload', this.selectedFile, this.selectedFile.name);
    const uploadUrl = this.schema.upload_url;
    this.http
      .post(uploadUrl, formData, {
        headers: new HttpHeaders({
          Accept: `text/plain`,
        }),
        responseType: 'text',
      })
      .subscribe(
        (response: string) => {
          this.uploadSuccess = true;
          this.fileUrl = response;
          if (this.fileUrl) {
            this.updateTextarea(this.fileUrl);
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
    const inputImageControl = this.formProperty.findRoot().getProperty(this.schema.input_mask);
    if (inputImageControl) {
      inputImageControl.setValue(url, false);
      this.isSaving = false;
      const element = document.getElementById(inputImageControl.__canonicalPathNotation);
      if (element) {
        element.classList.add('text-info');
      }
    }
  }
}
