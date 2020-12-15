import {Component, HostListener} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'QuickSplash';
  color = '';

  constructor(public router: Router) {
    if (localStorage.getItem('myData')) {
      this.color = JSON.parse(localStorage.getItem('myData')).color;
    }
  }

  top: any;
  left: any;
  followerTop: any;
  followerLeft: any;

  expand = false;

  @HostListener('document:click', ['$event'])
  // tslint:disable-next-line:typedef
    onClick($event) {
      this.expand = true;
      setTimeout(() => {
        this.expand = false;
      }, 500);
  }

  @HostListener('document:mousemove', ['$event'])
  // tslint:disable-next-line:typedef
    onMousemove($event) {
      this.top = ($event.pageY - 9) + 'px';
      this.left = ($event.pageX - 9) + 'px';


      setTimeout(() => {
        this.followerTop = ($event.pageY - 21) + 'px';
        this.followerLeft = ($event.pageX - 21) + 'px';
      }, 300);
  }

}
