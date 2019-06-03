import {
  AfterViewInit,
  Directive,
  ElementRef,
  HostListener,
  OnDestroy,
  Input,
} from '@angular/core';
import { NgModel } from '@angular/forms';
import { Subscription, Observable } from 'rxjs';

@Directive({
  selector: 'textarea[autosize]',
  host: {
    rows: '1',
    style: 'overflow: hidden',
  },
})
export class TextareaAutosizeDirective implements AfterViewInit, OnDestroy {

  private sub: Subscription;

  @Input() set autosize(observable: Observable<any>) {
    if (this.sub) {
      this.sub.unsubscribe();
    }
    if (observable) {
      this.sub = observable.subscribe(() => {
        this.resize();
      });
    }
  }

  constructor(
    private elem: ElementRef,
  ) {}

  public ngAfterViewInit() {
    this.resize();
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();
    }
  }

  @HostListener('input')
  private resize() {
    const textarea = this.elem.nativeElement as HTMLTextAreaElement;
    // Reset textarea height to auto that correctly calculate the new height
    textarea.style.height = 'auto';
    // Set new height
    textarea.style.height = `${textarea.scrollHeight + (textarea.offsetHeight - textarea.clientHeight)}px`;
  }
}
