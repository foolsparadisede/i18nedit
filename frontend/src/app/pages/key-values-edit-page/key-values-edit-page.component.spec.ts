import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyValuesEditPageComponent } from './key-values-edit-page.component';

describe('KeyValuesEditPageComponent', () => {
  let component: KeyValuesEditPageComponent;
  let fixture: ComponentFixture<KeyValuesEditPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KeyValuesEditPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeyValuesEditPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
