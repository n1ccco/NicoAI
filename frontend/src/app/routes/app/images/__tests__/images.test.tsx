import type { Mock } from 'vitest';

import '@testing-library/jest-dom';
import { ImagesRoute } from '@/app/routes/app/images/images';
import { createImage } from '@/testing/data-generators';
import {
  renderApp,
  screen,
  userEvent,
  waitFor,
  within,
} from '@/testing/test-utils';

beforeAll(() => {
  vi.spyOn(console, 'error').mockImplementation(() => {});
});

afterAll(() => {
  (console.error as Mock).mockRestore();
});
//todo: doesn't work
test(
  'should create, render and delete images',
  { timeout: 10000 },
  async () => {
    await renderApp(<ImagesRoute />);

    const newImage = createImage();

    expect(await screen.findByText(/no entries/i)).toBeInTheDocument();

    await userEvent.click(
      screen.getByRole('button', { name: /create image/i }),
    );

    const drawer = await screen.findByRole('dialog', {
      name: /generate image/i,
    });

    const promptField = within(drawer).getByText(/prompt/i);
    const negativePromptField = within(drawer).getByText(/negative prompt/i);
    const heightField = within(drawer).getByText(/height/i);
    const widthField = within(drawer).getByText(/width/i);
    const interStepsField = within(drawer).getByText(
      /number of interference steps/i,
    );
    const guidanceScale = within(drawer).getByText(/guidance scale/i);

    await userEvent.type(promptField, newImage.prompt);
    await userEvent.type(negativePromptField, newImage.negativePrompt);
    await userEvent.type(heightField, newImage.height.toString());
    await userEvent.type(widthField, newImage.width.toString());
    await userEvent.type(
      interStepsField,
      newImage.numInterferenceSteps.toString(),
    );
    await userEvent.type(guidanceScale, newImage.guidanceScale.toString());

    const submitButton = within(drawer).getByRole('button', {
      name: /submit/i,
    });

    await userEvent.click(submitButton);

    await waitFor(() => expect(drawer).not.toBeInTheDocument());

    expect(await screen.findByText(/delete image/i)).toBeInTheDocument();

    await userEvent.click(
      screen.getByRole('button', { name: /delete image/i }),
    );

    const confirmationDialog = await screen.findByRole('dialog', {
      name: /delete image/i,
    });

    const confirmationDeleteButton = within(confirmationDialog).getByRole(
      'button',
      {
        name: /delete image/i,
      },
    );

    await userEvent.click(confirmationDeleteButton);

    await screen.findByText(/image deleted/i);
  },
);
