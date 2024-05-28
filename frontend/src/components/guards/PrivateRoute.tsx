import { SIGNIN } from "@/constants/routeContants";
import { useAuth } from "@/hooks/useAuth";
import { Navigate, Outlet } from "react-router-dom";

export const PrivateRoute = () => {
    const {
      state: {
        type
      }
    } = useAuth();
    if (type === "authenticated") return <Navigate to={SIGNIN} />
    return <Outlet />
  }
  