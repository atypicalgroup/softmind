import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportUpdate } from './support-update';

describe('SupportUpdate', () => {
  let component: SupportUpdate;
  let fixture: ComponentFixture<SupportUpdate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupportUpdate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupportUpdate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
