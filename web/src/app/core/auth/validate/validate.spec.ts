import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Validate } from './validate';

describe('Validate', () => {
  let component: Validate;
  let fixture: ComponentFixture<Validate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Validate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Validate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
