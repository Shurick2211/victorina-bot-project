import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditVictorinaComponent } from './edit-victorina.component';

describe('EditVictorinaComponent', () => {
  let component: EditVictorinaComponent;
  let fixture: ComponentFixture<EditVictorinaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditVictorinaComponent]
    });
    fixture = TestBed.createComponent(EditVictorinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
