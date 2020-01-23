import { Address } from './../model/address';
import { Observable } from 'rxjs';
import { MagazineService } from './../services/magazine.service';
import { Component, OnInit } from '@angular/core';
import { Magazine } from '../model/magazine';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-magazine',
  templateUrl: './magazine.component.html',
  styleUrls: ['./magazine.component.css']
})
export class MagazineComponent implements OnInit {

  isVisible = false;
  addMagazineVisible = false;

  listOfActiveMagazines: Observable<Array<Magazine>>;
  currentMagazine: Magazine;

  magazineForm: FormGroup;
  addMagazineForm: FormGroup;
  isAdd = false;
  loading = false;
  submitted = false;
  error: string = null;

  constructor(
    private formBuilder: FormBuilder,
    private magazineService: MagazineService,
    private router: Router
  ) {

  }
  ngOnInit() {
    this.listOfActiveMagazines = this.magazineService.getAll();
    this.error = null;
  }
  createEditForm() {
    this.magazineForm = this.formBuilder.group({
      id: [this.currentMagazine.id],
      address: this.formBuilder.group({
        city: [this.currentMagazine.address.city, Validators.required],
        street: [this.currentMagazine.address.street, [Validators.required]],
        postalCode: [this.currentMagazine.address.postalCode, [Validators.required]],
        buildingNumber: [this.currentMagazine.address.buildingNumber, Validators.required],
        flatNumber: [this.currentMagazine.address.flatNumber, Validators.required]
      }),
      active: [this.currentMagazine.active, Validators.required]
    });
  }
  get f() { return this.magazineForm.controls; }

  get af() { return this.addMagazineForm.value; }

  edit(magazine: Magazine) {
    this.isVisible = !this.isVisible;
    this.currentMagazine = magazine;
    this.createEditForm();
  }

  onSubmit() {
    this.currentMagazine = new Magazine(this.magazineForm.value);
    this.magazineService.edit(this.currentMagazine)
      .subscribe(
        data => {
          this.ngOnInit();
        },
        error => {
          console.log(error.error.message);
          this.loading = false;
        },
      );
    this.listOfActiveMagazines = this.magazineService.getAll();
    this.isVisible = false;
    this.currentMagazine = null;
  }

  onCancel() {
    this.isVisible = false;
    this.currentMagazine = null;
    this.addMagazineVisible = false;
  }

  showDetails() {
    this.listOfActiveMagazines.forEach(data => {
      console.log(data);
    });
  }
  createAddForm() {
    this.addMagazineForm = this.formBuilder.group({
      address: this.formBuilder.group({
        city: ['', [Validators.required]],
        street: ['', [Validators.required]],
        postalCode: ['', [Validators.required]],
        buildingNumber: ['', Validators.required],
        flatNumber: ['', Validators.required]
      }),
    });
  }

  addMagazine() {
    this.addMagazineVisible = true;
    this.isAdd = false;
    this.createAddForm();
  }

  save() {
    this.submitted = true;
    const magazine = new Magazine(this.addMagazineForm.value);
    this.magazineService.save(magazine).subscribe(data => {
      this.isAdd = true;
      this.addMagazineVisible = false;
      this.ngOnInit();
    },
      error => {
        this.error = error.error.message;
      }
    );
  }

}
