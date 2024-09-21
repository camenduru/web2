/* eslint-disable */
import { Component } from '@angular/core';
import { ControlWidget } from 'ngx-schema-form';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'sf-example-widget',
  templateUrl: 'example.widget.html',
  imports: [CommonModule],
})
export class ExampleWidget extends ControlWidget {
  protected download(link: string): void {
    const url = `${link}`;
    window.open(url);
  }
}
