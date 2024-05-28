package com.wonkglorg.util.ip;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an IPv4
 */
public class IPv4 implements IP {

    int[] ip;

    /**
     * Creates a new ip from its 4 parts
     *
     * @param ip
     * @throws IllegalArgumentException if the ip has not 4 parts or a part is not between 0 and 255
     */
    public IPv4(int[] ip) throws IllegalArgumentException {
        if (ip.length != 4) {
            throw new IllegalArgumentException("IP must have 4 parts");
        }
        for (int i = 0; i < 4; i++) {
            if (ip[i] < 0 || ip[i] > 255) {
                throw new IllegalArgumentException("IP parts must be between 0 and 255");
            }
        }
        this.ip = ip;
    }

    /**
     * Creates a new ip from a string formatted as "xxx.xxx.xxx.xxx"
     *
     * @param ip
     * @throws IllegalArgumentException if the ip is not in the correct format
     */
    public IPv4(String ip) throws IllegalArgumentException {
        if (!validateIP(ip)) {
            throw new IllegalArgumentException("Invalid IP format");
        }
        this.ip = convertIp(ip).ip;
    }

    /**
     * Converts a string formatted as "xxx.xxx.xxx.xxx" to an IPv4
     *
     * @param ip the ip to convert
     * @return the IPv4
     * @throws IllegalArgumentException if the ip is not in the correct format
     */
    public static IPv4 convertIp(String ip) {
        try {
            String[] parts = ip.split("\\.");
            int[] ipArray = new int[4];
            for (int i = 0; i < 4; i++) {
                ipArray[i] = Integer.parseInt(parts[i]);
            }
            return new IPv4(ipArray);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid IP format");
        }

    }

    /**
     * Validates an ip string formatted as "xxx.xxx.xxx.xxx"
     *
     * @param ip the ip to validate
     * @return true if the ip is valid, false otherwise
     */
    public static boolean validateIP(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the ip as a string formatted as "xxx.xxx.xxx.xxx"
     *
     * @return
     */
    @Override
    public @NotNull String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(ip[i]);
            if (i < 3) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * Returns the ip as an array of integers
     *
     * @param subnetMask the subnet mask
     * @return the network address
     */
    public IPv4 getNetworkAddress(IPv4 subnetMask) {
        return IPv4.getNetworkAddress(this, subnetMask);
    }

    /**
     * Returns the broadcast address of the ip
     *
     * @param subnetMask the subnet mask
     * @return the broadcast address
     */
    public IPv4 getBroadcastAddress(IPv4 subnetMask) {
        return IPv4.getBroadcastAddress(this, subnetMask);
    }

    /**
     * Returns the network address
     *
     * @param ip         the ip
     * @param subnetMask the subnet mask
     * @return the network address
     */
    public static IPv4 getNetworkAddress(IPv4 ip, IPv4 subnetMask) {
        int[] networkAddress = new int[4];
        for (int i = 0; i < 4; i++) {
            networkAddress[i] = ip.ip[i] & subnetMask.ip[i];
        }
        return new IPv4(networkAddress);
    }

    /**
     * Returns the broadcast address
     *
     * @param ip         the ip
     * @param subnetMask the subnet mask
     * @return the broadcast address
     */
    public static IPv4 getBroadcastAddress(IPv4 ip, IPv4 subnetMask) {
        int[] broadcastAddress = new int[4];
        for (int i = 0; i < 4; i++) {
            broadcastAddress[i] = ip.ip[i] | ~subnetMask.ip[i];
        }
        return new IPv4(broadcastAddress);
    }

}
