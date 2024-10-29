import { test, expect } from '@playwright/test';

test('profile', async ({ page }) => {
  // update user:
  await page.goto('/app');
  await page.getByRole('button', { name: 'Open user menu' }).click();
  await page.getByRole('menuitem', { name: 'Your Profile' }).click();
  await page.getByRole('button', { name: 'Update Profile' }).click();
  await page.getByLabel('Username').click();
  await page.getByLabel('Username').fill('test-username');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page
    .getByLabel('Profile Updated')
    .getByRole('button', { name: 'Close' })
    .click();
  await expect(page.getByText('test-username')).toBeVisible();
});
