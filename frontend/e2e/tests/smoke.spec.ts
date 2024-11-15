import { test, expect } from '@playwright/test';

import { createImage, createComment } from '../../src/testing/data-generators';
test('smoke', async ({ page }) => {
  const image = createImage();
  const comment = createComment();

  await page.goto('/');
  await page.getByRole('button', { name: 'Get started' }).click();
  await page.waitForURL('/app');

  // create image:
  await page.getByRole('link', { name: 'Images' }).click();
  await page.waitForURL('/app/images');

  await page.getByRole('button', { name: 'Generate Image' }).click();
  await page.getByPlaceholder('Enter prompt').click();
  await page.getByPlaceholder('Enter prompt').fill(image.prompt);

  await page.getByRole('textbox', { name: 'Negative Prompt' }).click();
  await page
    .getByRole('textbox', { name: 'Negative Prompt' })
    .fill(image.negativePrompt);

  await page.getByLabel('Height').click();
  await page.getByLabel('Height').fill(image.height.toString());

  await page.getByLabel('Width').click();
  await page.getByLabel('Width').fill(image.width.toString());

  await page.getByLabel('Interference Steps').click();
  await page
    .getByLabel('Interference Steps')
    .fill(image.numInterferenceSteps.toString());

  await page.getByLabel('Guidance Scale').click();
  await page.getByLabel('Guidance Scale').fill(image.guidanceScale.toString());
  await page.getByRole('button', { name: 'Submit' }).click();

  await expect(page.getByText(image.prompt)).toBeVisible();

  // create comment:
  await page.getByRole('button', { name: 'Create Comment' }).click();
  await page.getByLabel('Body').click();
  await page.getByLabel('Body').fill(comment.body);
  await page.getByRole('button', { name: 'Submit' }).click();
  await expect(page.getByText(comment.body)).toBeVisible();
  await page
    .getByLabel('Comment Created')
    .getByRole('button', { name: 'Close' })
    .click();

  // delete comment:
  await page.getByRole('button', { name: 'Delete Comment' }).click();
  await expect(
    page.getByText('Are you sure you want to delete this comment?'),
  ).toBeVisible();
  await page.getByRole('button', { name: 'Delete Comment' }).click();
  await page
    .getByLabel('Comment Deleted')
    .getByRole('button', { name: 'Close' })
    .click();
  await expect(
    page.getByRole('heading', { name: 'No Comments Found' }),
  ).toBeVisible();
  await expect(page.getByText(comment.body)).toBeHidden();

  // delete image:
  await page.getByRole('button', { name: 'Delete Image' }).click();
  await page.getByRole('button', { name: 'Delete Image' }).click();
  await page
    .getByLabel('Image Deleted')
    .getByRole('button', { name: 'Close' })
    .click();
  await expect(
    page.getByRole('heading', { name: 'No Entries Found' }),
  ).toBeVisible();
});
