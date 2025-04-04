'use client';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { useState, type ReactNode } from 'react';
import { toast } from 'react-hot-toast';

interface QueryProviderProps {
  children: ReactNode;
}

export default function QueryProvider({ children }: QueryProviderProps) {
  // Create a client
  const [queryClient] = useState(
    () =>
      new QueryClient({
        defaultOptions: {
          queries: {
            staleTime: 60 * 1000, // 1 minute
            refetchOnWindowFocus: false,
            retry: 1,
          },
        },
      })
  );

  // Set up global error handling
  queryClient.setDefaultOptions({
    queries: {
      onSettled: (_data, error) => {
        if (error) {
          toast.error(error instanceof Error ? error.message : 'An error occurred');
        }
      },
    },
    mutations: {
      onSettled: (_data, _variables, _context, error) => {
        if (error) {
          toast.error(error instanceof Error ? error.message : 'An error occurred');
        }
      },
    },
  });

  return (
    <QueryClientProvider client={queryClient}>
      {children}
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
} 