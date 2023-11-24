import { Injectable } from '@angular/core';
import {Victorina} from "../dto/victorina";
import {ApiService} from "./api.service";
import {Person} from "../dto/person";


@Injectable({
  providedIn: 'root'
})
export class StorageService {

  public victorinas:Victorina[];

  public userId:string | null = null

  public person:Person | null = null

  constructor(private api:ApiService) {
    this.victorinas = new Array<Victorina>()
  }

  refreshPerson(){
    if (this.userId !== null)
    this.api.getPerson(this.userId).subscribe( response => {
      this.person = response.body
      console.log(this.person?.userName)
    })
  }

  refreshVictorins(){
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
    this.refreshVictorins()
  }

  delete(i: number) {
    this.api.deleteVictorina(`${this.victorinas[i].id}`).subscribe(response => {
      console.log(response)    })
    this.refreshVictorins()
  }



}
