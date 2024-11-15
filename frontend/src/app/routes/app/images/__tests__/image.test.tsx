import { ImageRoute } from '@/app/routes/app/images/image';
import {
  renderApp,
  screen,
  userEvent,
  waitFor,
  createImage,
  createUser,
  within,
} from '@/testing/test-utils';
import '@testing-library/jest-dom';

const renderImage = async () => {
  const fakeUser = await createUser();
  const fakeImage = await createImage();

  const utils = await renderApp(<ImageRoute />, {
    user: fakeUser,
    path: `/app/images/:imageId`,
    url: `/app/images/${fakeImage.id}`,
  });

  await screen.findByText(fakeImage.id);

  return {
    ...utils,
    fakeUser,
    fakeImage,
  };
};

//todo: doesn't work

test('should render image', async () => {
  const { fakeImage } = await renderImage();
  expect(screen.getByText(fakeImage.id)).toBeInTheDocument();
});

test(
  'should create and delete a comment on the image',
  async () => {
    await renderImage();

    const comment = 'Hello World';

    await userEvent.click(
      screen.getByRole('button', { name: /create comment/i }),
    );

    const drawer = await screen.findByRole('dialog', {
      name: /create comment/i,
    });

    const bodyField = await within(drawer).findByText(/body/i);

    await userEvent.type(bodyField, comment);

    const submitButton = await within(drawer).findByRole('button', {
      name: /submit/i,
    });

    await userEvent.click(submitButton);

    await waitFor(() => expect(drawer).not.toBeInTheDocument());

    await screen.findByText(comment);

    const commentsList = await screen.findByRole('list', {
      name: 'comments',
    });

    const commentElements =
      await within(commentsList).findAllByRole('listitem');

    const commentElement = commentElements[0];

    expect(commentElement).toBeInTheDocument();

    const deleteCommentButton = within(commentElement).getByRole('button', {
      name: /delete comment/i,
      // exact: false,
    });

    await userEvent.click(deleteCommentButton);

    const confirmationDialog = await screen.findByRole('dialog', {
      name: /delete comment/i,
    });

    const confirmationDeleteButton = await within(
      confirmationDialog,
    ).findByRole('button', {
      name: /delete/i,
    });

    await userEvent.click(confirmationDeleteButton);

    await screen.findByText(/comment deleted/i);

    await waitFor(() => {
      expect(within(commentsList).queryByText(comment)).not.toBeInTheDocument();
    });
  },
  {
    timeout: 20000,
  },
);
