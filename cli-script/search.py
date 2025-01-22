import requests
import json

def make_post_request(search_string, payload):
    url = "http://localhost:3000/logs-search" 
    headers = {
        "Content-Type": "application/json",
    }
    params = {
        "searchString": search_string
    }
    response = requests.post(url, headers = headers, params = params, json = payload)
    return response.json()

def get_user_input(prompt):
    user_input = input(prompt).strip()
    return user_input if len(user_input) != 0 else None

def get_payload():
    level = get_user_input("\tEnter level (or leave blank): ")
    message = get_user_input("\tEnter message (or leave blank): ")
    resourceId = get_user_input("\tEnter resourceId (or leave blank): ")
    traceId = get_user_input("\tEnter traceId (or leave blank): ")
    spanId = get_user_input("\tEnter spanId (or leave blank): ")
    commit = get_user_input("\tEnter commit (or leave blank): ")
    parentResourceId = get_user_input("\tEnter parentResourceId (or leave blank): ")
    startTime = get_user_input("\tEnter startTime (or leave blank): ")
    endTime = get_user_input("\tEnter endTime (or leave blank): ")

    payload = {
        "level": level,
        "message": message,
        "resourceId": resourceId,
        "traceId": traceId,
        "spanId": spanId,
        "commit": commit,
        "metadata": {"parentResourceId": parentResourceId},
        "startTime": startTime,
        "endTime": endTime
    }
    return payload

def main():
    print("\nWelcome to Log Search Command Line Interface")
    while(True):
        search_string = input("\nEnter search string: ")
        use_json_file = input("\nDo you want to provide a JSON file for the filter? (y/n): ").lower()

        if use_json_file == 'y':
            json_file_path = input("Enter the path to your filter JSON file: ")
            with open(json_file_path, 'r') as file:
                payload = json.load(file)
        else:
            print("\nEnter the filter maunually. Leave blank if you don't want to apply filter for that respective field.")
            payload = get_payload()

        response = make_post_request(search_string, payload)

        print("Response:")
        print(json.dumps(response, indent=2))

        go_again = input("\nGo again? (y/n): ").lower()
        if go_again != 'y':
            print("Exiting....")
            break;


if __name__ == "__main__":
    main()
