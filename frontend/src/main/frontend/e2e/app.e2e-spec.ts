import { NinegoldPage } from './app.po';

describe('ninegold App', () => {
  let page: NinegoldPage;

  beforeEach(() => {
    page = new NinegoldPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
