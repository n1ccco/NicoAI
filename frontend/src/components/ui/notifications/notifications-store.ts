import { nanoid } from 'nanoid';
import { create } from 'zustand';

export type Notification = {
  id: string;
  type: 'info' | 'warning' | 'success' | 'error';
  title: string;
  message?: string;
};

type NotificationsStore = {
  notifications: Notification[];
  addNotification: (notification: Omit<Notification, 'id'>) => void;
  dismissNotification: (id: string) => void;
};

export const useNotifications = create<NotificationsStore>((set) => ({
  notifications: [],
  addNotification: (notification) => {
    const id = nanoid();
    set((state) => ({
      notifications: [...state.notifications, { id, ...notification }],
    }));
    setTimeout(() => {
      set((state) => ({
        notifications: state.notifications.filter((notif) => notif.id !== id),
      }));
    }, 3000);
  },
  dismissNotification: (id) =>
    set((state) => ({
      notifications: state.notifications.filter(
        (notification) => notification.id !== id,
      ),
    })),
}));
