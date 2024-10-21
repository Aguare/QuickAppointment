import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'image',
  standalone: true
})
export class ImagePipe implements PipeTransform {

  transform(imagePath: string): string {
    const port = 'http://localhost:8000';    
    return `${port}${imagePath}`;
  }

}
