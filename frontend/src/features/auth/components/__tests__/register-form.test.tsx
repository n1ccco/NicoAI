import { createUser } from '@/testing/data-generators';
import { renderApp, screen, userEvent, waitFor } from '@/testing/test-utils';

import { RegisterForm } from '../register-form';

test('should register new user and call onSuccess cb which should navigate the user to the app', async () => {
  const newUser = createUser({});

  const onSuccess = vi.fn();

  await renderApp(<RegisterForm onSuccess={onSuccess} chooseTeam={false} />, {
    user: null,
  });

  await userEvent.type(screen.getByLabelText(/Username/i), newUser.username);
  await userEvent.type(screen.getByLabelText(/password/i), newUser.password);
  await userEvent.click(screen.getByRole('button', { name: /register/i }));

  await waitFor(() => expect(onSuccess).toHaveBeenCalledTimes(1));
});
