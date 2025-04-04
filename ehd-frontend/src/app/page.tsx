import { Suspense } from 'react';
import Dashboard from '@/components/Dashboard';
import LoadingSpinner from '@/components/LoadingSpinner';
import AppLayout from '@/components/layout/AppLayout';

export default function HomePage() {
  return (
    <AppLayout>
      <main className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold mb-8">Employee Hardware Directory</h1>
        <Suspense fallback={<LoadingSpinner />}>
          <Dashboard />
        </Suspense>
      </main>
    </AppLayout>
  );
}
