import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KeyValueEditComponent } from './key-value-edit.component';

describe('KeyValueEditComponent', () => {
  let component: KeyValueEditComponent;
  let fixture: ComponentFixture<KeyValueEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KeyValueEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeyValueEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
