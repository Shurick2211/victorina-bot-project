import { Injectable } from '@angular/core';
import {Victorina} from "../dto/victorina";
import {ApiService} from "./api.service";

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  public victorinas:Victorina[];
  constructor(private api:ApiService) {
    this.victorinas = new Array<Victorina>()
    this.refresh()
  }

  refresh(){
    this.api.getAllVictorinas().subscribe(response => {
      this.victorinas = response
      console.log(this.victorinas)
    })
  }

  save(victorina: Victorina ){
    this.api.createVictorina(victorina).subscribe(
      response => {
        console.log(response.status)
      })
  }

  delete(i: number) {
    this.api.deleteVictorina(`${this.victorinas[i].id}`).subscribe(response => {
      console.log(response)    })
    this.refresh()
  }

}
