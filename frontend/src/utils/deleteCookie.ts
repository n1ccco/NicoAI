export const deleteCookie = (name : string) : void => {
  document.cookie = name + '=; Max-Age=-99999999;';
}