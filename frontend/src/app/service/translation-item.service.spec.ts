import { TestBed } from '@angular/core/testing';

import { TranslationItemService } from './translation-item.service';

describe('TranslationItemService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TranslationItemService = TestBed.get(TranslationItemService);
    expect(service).toBeTruthy();
  });
});
