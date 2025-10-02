import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportDelete } from './support-delete';

describe('SupportDelete', () => {
  let component: SupportDelete;
  let fixture: ComponentFixture<SupportDelete>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupportDelete]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupportDelete);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
