import * as React from 'react';

import { cn } from '@/utils/cn';

export const ImageGalleryContainer = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    className={cn(
      'grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4',
      className,
    )}
    {...props}
  />
));
ImageGalleryContainer.displayName = 'ImageGalleryContainer';

export const ImageCard = React.forwardRef<
  HTMLDivElement,
  React.HTMLAttributes<HTMLDivElement>
>(({ className, children, ...props }, ref) => (
  <div
    ref={ref}
    className={cn(
      'relative overflow-hidden rounded-lg shadow-lg transition-transform hover:scale-105 bg-gray-800 p-6',
      className,
    )}
    {...props}
  >
    {children}
  </div>
));
ImageCard.displayName = 'ImageCard';

export const ImageCaption = React.forwardRef<
  HTMLParagraphElement,
  React.HTMLAttributes<HTMLParagraphElement>
>(({ className, ...props }, ref) => (
  <p
    ref={ref}
    className={cn('mt-2 text-sm text-muted-foreground text-center', className)}
    {...props}
  />
));
ImageCaption.displayName = 'ImageCaption';
