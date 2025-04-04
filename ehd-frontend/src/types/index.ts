// Hardware Asset Types
export interface HardwareAsset {
  id: string;
  serialNumber: string;
  model: string;
  manufacturer: string;
  purchaseDate: string;
  warrantyEndDate: string;
  status: HardwareStatus;
  hardwareType: HardwareType;
  currentAssignment?: AssignmentHistory;
  assignmentHistory: AssignmentHistory[];
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export enum HardwareStatus {
  AVAILABLE = 'AVAILABLE',
  ASSIGNED = 'ASSIGNED',
  MAINTENANCE = 'MAINTENANCE',
  RETIRED = 'RETIRED',
  LOST = 'LOST'
}

// Hardware Type Types
export interface HardwareType {
  id: string;
  name: string;
  description: string;
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
  employeeId: string;
  firstName: string;
  lastName: string;
  email: string;
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