import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'increment'
})
export class IncrementPipe implements PipeTransform {
  transform(value: any, args?: any): any {
    return value + 1;
  }
}
