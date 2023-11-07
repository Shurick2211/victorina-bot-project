import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateVictorinaComponent } from './create-victorina.component';

describe('CreateVictorinaComponent', () => {
  let component: CreateVictorinaComponent;
  let fixture: ComponentFixture<CreateVictorinaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateVictorinaComponent]
    });
    fixture = TestBed.createComponent(CreateVictorinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
