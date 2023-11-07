import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListVictorinasComponent } from './list-victorinas.component';

describe('ListVictorinasComponent', () => {
  let component: ListVictorinasComponent;
  let fixture: ComponentFixture<ListVictorinasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListVictorinasComponent]
    });
    fixture = TestBed.createComponent(ListVictorinasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
