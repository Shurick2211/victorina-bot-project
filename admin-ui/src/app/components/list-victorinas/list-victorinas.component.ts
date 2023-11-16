import {Component, OnInit} from '@angular/core';
import {Victorina} from "../../dto/victorina";
import {ApiService} from "../../services/api.service";

@Component({
  selector: 'app-list-victorinas',
  templateUrl: './list-victorinas.component.html',
  styleUrls: ['./list-victorinas.component.css']
})
export class ListVictorinasComponent implements OnInit{
  victorinas = new Array<Victorina>();


  constructor(private apiService:ApiService) {

  }

  ngOnInit(): void {
    this.getAll()
  }



  delete(i: number) {
    this.apiService.deleteVictorina(`${this.victorinas[i].id}`).subscribe(response => {
      console.log(response)
    })
    this.getAll()
  }

  private getAll(){
    this.apiService.getAllVictorinas().subscribe(response => {
      this.victorinas = response
      console.log(this.victorinas)
    })
  }
}
