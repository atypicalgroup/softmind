import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportCreate } from './support-create';

describe('SupportCreate', () => {
  let component: SupportCreate;
  let fixture: ComponentFixture<SupportCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupportCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupportCreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
