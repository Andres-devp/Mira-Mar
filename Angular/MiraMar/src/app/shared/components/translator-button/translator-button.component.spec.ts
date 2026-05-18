import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TranslatorButtonComponent } from './translator-button.component';

describe('TranslatorButtonComponent', () => {
  let component: TranslatorButtonComponent;
  let fixture: ComponentFixture<TranslatorButtonComponent>;

  beforeEach(async) {
    await TestBed.configureTestingModule({
      declarations: [ TranslatorButtonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TranslatorButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
