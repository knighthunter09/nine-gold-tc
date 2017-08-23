import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ServicesGenerationComponent } from './services-generation.component';

describe('ServicesGenerationComponent', () => {
  let component: ServicesGenerationComponent;
  let fixture: ComponentFixture<ServicesGenerationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ServicesGenerationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ServicesGenerationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
