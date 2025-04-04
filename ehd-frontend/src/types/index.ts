// Hardware Asset Types
export interface HardwareAsset {
  id: string;
  serialNumber: string;
  model: string;
  manufacturer: string;
  status: HardwareStatus;
  assignedTo?: Employee;
  purchaseDate: string;
  warrantyEndDate: string;
  notes?: string;
  hardwareType: HardwareType;
  currentAssignment?: AssignmentHistory;
  assignmentHistory: AssignmentHistory[];
  createdAt: string;
  updatedAt: string;
}

export type HardwareStatus = 'Available' | 'Assigned' | 'Maintenance' | 'Retired' | 'Damaged';

// Hardware Type Types
export interface HardwareType {
  id: string;
  name: string;
  description?: string;
  category: HardwareCategory;
  createdAt: string;
  updatedAt: string;
}

export enum HardwareCategory {
  LAPTOP = 'LAPTOP',
  DESKTOP = 'DESKTOP',
  MONITOR = 'MONITOR',
  PERIPHERAL = 'PERIPHERAL',
  NETWORK = 'NETWORK',
  SERVER = 'SERVER',
  OTHER = 'OTHER'
}

// Employee Types
export interface Employee {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  department: string;
  position: string;
  location: string;
  isActive: boolean;
  currentAssets: HardwareAsset[];
  assignmentHistory: AssignmentHistory[];
  createdAt: string;
  updatedAt: string;
}

// Assignment History Types
export interface AssignmentHistory {
  id: string;
  hardwareAsset: HardwareAsset;
  employee: Employee;
  assignedDate: string;
  returnedDate?: string;
  notes?: string;
  status: AssignmentStatus;
  createdAt: string;
  updatedAt: string;
}

export enum AssignmentStatus {
  ACTIVE = 'ACTIVE',
  RETURNED = 'RETURNED',
  LOST = 'LOST',
  DAMAGED = 'DAMAGED'
}

// User Types (for authentication)
export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  department?: string;
  isActive: boolean;
  lastLogin?: string;
  createdAt: string;
  updatedAt: string;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER',
  USER = 'USER'
}

// API Response Types
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

export interface PaginatedResponse<T> {
  data: T[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
} 