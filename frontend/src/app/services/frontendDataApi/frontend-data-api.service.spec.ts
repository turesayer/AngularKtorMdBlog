import { TestBed } from '@angular/core/testing';

import { FrontendDataApiService } from './frontend-data-api.service';

describe('FrontendDataApiService', () => {
  let service: FrontendDataApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrontendDataApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
